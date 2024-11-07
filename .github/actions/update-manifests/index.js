const core = require('@actions/core');
const exec = require('@actions/exec');
const github = require('@actions/github');
const fs = require('fs');
const path = require('path');

async function run() {
  try {
    // Retrieve the JSON-like input for repos, folders, and tokens
    const reposAndFolders = JSON.parse(core.getInput('repos_and_folders'));
    const imageTag = core.getInput('image_tag');

    // Helper function to increment appVersion
    async function incrementVersion(chartPath, envVarName) {
      const { stdout: currentVersionLine } = await exec.getExecOutput('grep', ['^appVersion:', `${chartPath}/Chart.yaml`]);
      const { stdout: currentVersion } = await exec.getExecOutput('cut', ['-d', ' ', '-f', '2'], { input: currentVersionLine });

      const trimmedVersion = currentVersion.trim().replace(/"/g, '');
      const currentDate = new Date().toISOString().slice(0, 7).replace('-', '.');
      let newVersion;

      if (trimmedVersion.startsWith(currentDate)) {
        const patchNumber = parseInt(trimmedVersion.split('.')[2]) + 1;
        newVersion = `${currentDate}.${patchNumber}`;
      } else {
        newVersion = `${currentDate}.0`;
      }

      // Set the new version as an environment variable
      core.exportVariable(envVarName, newVersion);
      return newVersion;
    }

    // Loop through each repository-folder-token mapping
    for (let i = 0; i < reposAndFolders.length; i++) {
      const { repo, folder, token } = reposAndFolders[i];

      console.log(`Updating ${repo} in folder ${folder} using secret ${token}`);

      // Extract the repository name from the URL
      const repoName = repo.split('/').pop().replace('.git', '');

      // Clone the repository using the provided token
      await exec.exec('git', ['clone', `https://x-access-token:${process.env[token]}@github.com/${repo}`]);
      process.chdir(repoName);

      // Set up Git configuration for committing changes
      await exec.exec('git config --global user.name "GitHub-Actions-CI-Build"');
      await exec.exec('git config --global user.email "user@gitHubActions.com"');

      // Check if the folder exists in the repo before continuing
      const folderPath = path.join(process.cwd(), folder);
      if (!fs.existsSync(folderPath)) {
        console.log(`Skipping ${repo} as folder ${folder} does not exist.`);
        process.chdir('..');
        await exec.exec(`rm -rf ${repoName}`);
        continue;
      }

      // Step 1: Update Helm values with the new image tag
      console.log('Updating Helm values with the new image tag...');
      await exec.exec(`sed -i "s/tag: .*/tag: ${imageTag}/" ${folder}/values.yaml`);

      // Step 2: Increment appVersion for each chart, based on the folder
      console.log('Incrementing appVersion for Argo CD and Rollouts...');
      const newVersion = await incrementVersion(folder, `new_version_${folder.replace(/-/g, '_').toLowerCase()}`);

      // Step 3: Update appVersion in Chart.yaml for each folder
      console.log('Updating appVersion in Chart.yaml files...');
      await exec.exec(`sed -i "s/^appVersion:.*/appVersion: \\"${newVersion}\\"/" ${folder}/Chart.yaml`);
      console.log(`Updated appVersion in ${folder}/Chart.yaml to ${newVersion}`);

      // Step 4: Check for changes, commit if necessary, and create a pull request if not on main
      console.log('Checking for changes...');
      await exec.exec('git add .');
      const { exitCode } = await exec.getExecOutput('git diff-index --quiet HEAD --');

      if (exitCode !== 0) {
        const branchName = `update-feature/${github.context.ref_name}-${new Date().toISOString().replace(/[-:.]/g, '')}`;

        if (github.context.ref === 'refs/heads/main') {
          console.log('Committing changes to main...');
          await exec.exec(`git commit -m "Updated image tag to ${imageTag} and incremented app versions"`);
          await exec.exec('git push origin main');
        } else {
          console.log('Creating a new branch and pull request...');
          await exec.exec(`git checkout -b ${branchName}`);
          await exec.exec(`git commit -m "Update image tag to ${imageTag} and incremented app versions"`);
          await exec.exec(`git push -u origin ${branchName}`);

          // Create the pull request using GitHub CLI
          await exec.exec(`gh pr create --title "Updated image tag to ${imageTag}" --body "This PR updates the image tag to ${imageTag}."`);
        }
      } else {
        console.log('No changes detected.');
      }

      // Clean up by removing the cloned repo
      process.chdir('..');
      await exec.exec(`rm -rf ${repoName}`);
    }
  } catch (error) {
    core.setFailed(error.message);
  }
}

run();
