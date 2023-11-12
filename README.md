# How to merge your brach into DEV
## 1. Ensure Your Branch is Up-to-Date:
Before merging, make sure your branch is up-to-date with the latest changes from the main branch. You can do this by pulling the latest changes:

```bash
git checkout dev
git pull origin dev
git checkout your-feature-branch
```
## 2. Resolve Conflicts Locally:
If there are any conflicts between your branch and the main branch, resolve them locally before attempting to merge. Use a merge tool or manually edit the conflicting files.

```bash
git merge dev
```
If conflicts occur, Git will mark the conflicted files. After resolving conflicts, mark them as resolved:

```bash
git add <conflicted-file>
```

## 3. Test Your Changes:
Before merging, ensure that your changes work as expected. Run tests and perform any necessary validation.

## 4. Commit Your Changes:
Commit your changes to the feature branch after resolving conflicts and testing:

```bash
git commit -m "Merge main into feature branch and resolve conflicts"
```

## 5. Push Your Branch:
Push your branch with the new merge commit to the remote repository:

```bash
git push dev your-feature-branch
```

## 6. Create a Pull Request (if using a platform like GitHub):
If you're working with a platform like GitHub, GitLab, or Bitbucket, create a pull request (PR). This allows your changes to be reviewed before merging into the main branch.
