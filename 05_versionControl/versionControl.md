# Version Control

## Inventing Version Control

Some key operations of a version control scheme is:

- *reverting* to a past version
- *comparing* two different versions
- *pushing* full version history to another location
- *pulling* history back from that location
- *merging* versions that are offshoots of the same earlier version

### Distributed VS Centralized

**Centralized** version control systems (like CVS and Subversion) has one master server and copies that only communicate with the master. Everyone must share their work to and from the master repository, so there's only one collaboration graph.

**Distributed Systems** like Git and Mercurial allow different collaboration graphs, where teams can experiment with alternate versions of repos, and only merging versions together where appropriate.

### Terminology

- **Repository** : a local or remote store of the versions in our project
- **Working copy** : a local, editable copy of our project that we can work on
- **File** : a single file in our project
- **Version or revision** : a record of the contents of our project at a point in time
- **Change or diff** : the difference between two versions
- **Head** : the current version

### Features

- **Reliable** : keep versions around for as long as we need them; allow backups
- **Multiple files** : track versions of a project, not single files
- **Meaningful versions** : what were the changes, why where they made?
- **Revert** : restore old versions, in whole or in part
- **Compare versions**
- **Review history** : for the whole project or individual files

## Git Basics

### Getting a Git Repo

For initializing a repo in an exisiting directory, we use  `git init` , then track files with `git add` and make the initial commit with `git commit`. For cloning an existing repo, we use `git clone <url>`.

### Recording Changes to the Repo

<img src="image.png" width="600">

*Lifecycle of files*

Each file in our working directory is either *tracked* or *untracked*. Tracked files are recognised by git, and are either *unmodifed, modified, or staged*. When you first clone a repo, all files will be tracked an unmodified. Creating new files will be untracked intially. As you edit files, these become modified, and as you work you selectively stage these modified files with `add` and then commit those stage changes with `commit `.

**Checking status of files**

We use `git status` to check which files are in which state.

```shell
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working tree clean
```

means that you have a clean working directory - none of your tracked files are modified (Git doesnt see your untracked files)

```shell
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Untracked files:
  (use "git add <file>..." to include in what will be committed)

    README

nothing added to commit but untracked files present (use "git add" to track)
```

If you add a new `README` file, you can see there is a new untracked file.

**Tracking New Files**

We use `git add` to track new files; you can also use a directory for this command and it'll add the whole directory


**Staging Modified Files**

If you change a previously tracked file called `CONTRIBUTING.md` and run `git status`, you'll get

```shell
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    new file:   README

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```
This means a file has been modified in the working directory but changes not staged. We again use `git add` to stage our modifications. Note that if you modify a file after running `git add`, you have to do `git add` again to stage the latest version of the file.

**Ignoring Files**

We use `.gitignore` to create a file listing patterns to match ignored files.

- Standard Glob patterns
  - `*` matches zero or more characters
  - `[abc]` matches any of the characters inside brackets; `a`,`b`, or `c`
  - `?` matches a single character
  - `[0-9]` matches `0`,`1`....`9` etc this applies for characters too
  - `a/**/z` matches nested folders like `a/b/z` or `a/b/c/z`
- Start patterns with a forward slash `/` to avoid recursitivity
- End patterns with `/` to specify a directory
- Negate patterns by starting with `!`

```shell
# ignore all .a files
*.a

# but do track lib.a, even though you're ignoring .a files above
!lib.a

# only ignore the TODO file in the current directory, not subdir/TODO
/TODO

# ignore all files in any directory named build
build/

# ignore doc/notes.txt, but not doc/server/arch.txt
doc/*.txt

# ignore all .pdf files in the doc/ directory and any of its subdirectories
doc/**/*.pdf
```

**View Staged and Unstaged Changes**

`git diff` provides more details than `git status`. This answers 2 questions: "What have you changed but not staged?" "What have you staged and are about to commit?"

If we edit and stage `README` again and edit `CONTRIBUTING` again without staging it, then run `git status`:

```shell
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    modified:   README

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```

Whereas `git diff` will give us:
```shell
$ git diff
diff --git a/CONTRIBUTING.md b/CONTRIBUTING.md
index 8ebb991..643e24f 100644
--- a/CONTRIBUTING.md
+++ b/CONTRIBUTING.md
@@ -65,7 +65,8 @@ branch directly, things can get messy.
 Please include a nice description of your changes when you submit your PR;
 if we have to read the whole diff to figure out why you're contributing
 in the first place, you're less likely to get feedback and have your change
-merged in.
+merged in. Also, split your changes into comprehensive chunks if your patch is
+longer than a dozen lines.

 If you are starting to work on a particular area, feel free to submit a PR
 that highlights your work in progress (and note in the PR title that it's
 ```

 Which goes into exact detail which line we havent staged. `git diff` only shows changes that are still unstaged.

 **Committing Your Changes**

 Only stuff you staged will go into commit. Using `git commit` will commit your changes.

 **Skipping Staging**

If you want to skip the staging area, Git provides a simple shortcut. Using `git commit -a` to the `commit` command makes Git automatically stage every file that is already tracked before doing the commit, letting you skip the `git add` part.

**Removing Files**

To remove a file from Git, we must remove it from the tracked files (remove from staging area) and then commit. `git rm` removes the file from working directory completely. Using `rm` will remove the files from working directory and using `git rm` stages the file's removal. If you modified the file or added it to staging area, you must force removal with `-f` option.

You may want to keep the file in working directory but remove it from staging area (not have Git track it anymore). For example, we forgot to add something to `.gitignore` and you want to un-stage it. We use the `--cached` to only remove from staging area and not working directory: `git rm --cached README`

**Moving Files**

Git doesn’t explicitly track file movement. If you rename a file in Git, no metadata is stored in Git that tells it you renamed the file. If you want to rename a file in Git, you do `git mv file_from file_to`. This is equivalent to:

```shell
$ mv README.md README
$ git rm README.md
$ git add README
```
and Git figures out its a rename implicitly.

### Viewing the Commit History

Use `git log` to view the previous commit history, and `q` to exit the log.

We use `git log --stat` to see the condensed history. This shows a list of modified files, and how many lines in each file was added or removed.
```shell
$ git log --stat
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    Change version number

 Rakefile | 2 +-
 1 file changed, 1 insertion(+), 1 deletion(-)

commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700

    Remove unnecessary test

 lib/simplegit.rb | 5 -----
 1 file changed, 5 deletions(-)

commit a11bef06a3f659402fe7563abf99ad00de2209e6
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 10:31:28 2008 -0700

    Initial commit

 README           |  6 ++++++
 Rakefile         | 23 +++++++++++++++++++++++
 lib/simplegit.rb | 25 +++++++++++++++++++++++++
 3 files changed, 54 insertions(+)
 ```

`git log --graph` also makes a ASCII graph showing the branch and merge history:
```shell
$ git log --pretty=format:"%h %s" --graph
* 2d3acf9 Ignore errors from SIGCHLD on trap
*  5e3ee11 Merge branch 'master' of https://github.com/dustin/grit.git
|\
| * 420eac9 Add method for getting the current branch
* | 30e367c Timeout code and tests
* | 5a09431 Add timeout protection to grit
* | e1193f8 Support for heads with slashes in them
|/
* d6016bc Require time for xmlschema
*  11d191e Merge branch 'defunkt' into local
```

### Undoing Things

