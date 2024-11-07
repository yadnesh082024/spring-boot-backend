const core = require('@actions/core');
const exec = require('@actions/exec');
const github = require('@actions/github');

async function run() {
  try {
    const repos = core.getInput('repos').split('\n').map(repo => repo.trim()).filter(repo => repo);
    const folders = core.getInput('folders').split('\n').map(folder => folder.trim()).filter(folder => folder);
    const secrets = core.getInput('secrets').split('\n').map(secret => secret.trim()).filter(secret => secret);
    const imageTag = core.getInput('image_tag');

    for (let i = 0; i < repos.length; i++) {
      const repo = repos[i];
      const folder = folders[i];
      const secret = secrets[i];

      console.log(`Updating ${repo} in folder ${folder} using secret ${secret}`);

      // Extract the repository name from the URL
      const repoName = repo.split('/').pop().replace('.git', '');

      // Checkout the repository using actions/checkout
      await exec.exec('git', ['clone', `https://x-access-token:${process.env[secret]}@github.com/${repoName}.git`]);
      process.chdir(repoName);

      // Set up Git configuration for committing changes
      await exec.exec('git config --global user.name "GitHub-Actions-CI-Build"');
      await exec.exec('git config --global user.email "user@gitHubActions.com"');

      // Update Helm values with the new image tag
      await exec.exec(`sed -i "s/tag: .*/tag: ${imageTag}/" ${folder}/values.yaml`);

      // Extract and increment appVersion for Argo CD and rollouts
      const { stdout: currentVersion } = await exec.getExecOutput(`grep '^appVersion:' ${folder}/Chart.yaml | cut -d ' ' -f 2 | tr -d '"'`);
      const currentDate = new Date().toISOString().slice(0, 7).replace('-', '.');
      let newVersion;

      if (currentVersion.startsWith(currentDate)) {
        const patchNumber = parseInt(currentVersion.split('.')[2]) + 1;
        newVersion = `${currentDate}.${patchNumber}`;
      } else {
        newVersion = `${currentDate}.0`;
      }

      await exec.exec(`sed -i "s/^appVersion:.*/appVersion: \\"${newVersion}\\"/" ${folder}/Chart.yaml`);

      // Check for changes, commit if necessary, and create a pull request if not on main
      await exec.exec('git add .');
      const { exitCode } = await exec.getExecOutput('git diff-index --quiet HEAD --');

      if (exitCode !== 0) {
        if (github.context.ref === 'refs/heads/main') {
          await exec.exec(`git commit -m "Updated image tag to ${imageTag} and incremented app versions"`);
          await exec.exec('git push origin main');
        } else {
          const timestamp = new Date().toISOString().replace(/[-:.]/g, '');
          const branchName = `update-feature/${github.context.ref_name}-${timestamp}`;
          await exec.exec(`git checkout -b ${branchName}`);
          await exec.exec(`git commit -m "Update image tag to ${imageTag} and incremented app versions"`);
          await exec.exec(`git push -u origin ${branchName}`);
          await exec.exec(`gh pr create --title "Updated image tag to ${imageTag}" --body "This PR updates the image tag to ${imageTag}."`);
        }
      } else {
        console.log('No changes detected.');
      }

      process.chdir('..');
      await exec.exec(`rm -rf ${repoName}`);
    }
  } catch (error) {
    core.setFailed(error.message);
  }
}

run();
