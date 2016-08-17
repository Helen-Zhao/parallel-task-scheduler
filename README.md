# softeng-306-p1

Development Workflow!

##1) Forking the repository
On the Github GUI for github.com/Helen-Zhao/softeng-306-p1, click the "fork" button on the top right corner. Select your own account (i.e. instead of Helen-Zhao/softeng-306-p1 choose John-Doe/softeng-306-p1). Then, navigate to your newly forked directory on github (github.com/John-Doe/softeng-306-p1) and copy the "clone or download" link.  

##2) Cloning into the repo with SSH
Navigate to the folder you want to store your local copy of the repository. Right click, `Open Git BASH here`. If you're using SSH, simply type `git clone git@github.com:John-Doe/softeng-306-p1.git` into the terminal (the repository you're cloning into being the one that was just forked). You can create as many branches and stuff if you want

##3) Setting up remotes
Use the command `git remote -v` to see your list of currently set up remotes. "origin" should be your fork, but if it's not it's okay. If you do not have the main branch (Helen-Zhao/softeng-306-p1) on your list of remotes, add it using `git remote add git@github.com:Helen-Zhao/softeng-306-p1.git upstream`. The `upstream` is the name of the main dev remote, you can name it something else if you want. Use `git remote -v` again to double check the remote has been added.

##4) Writing Code [Coding Style]
 - Variable names should use Camel Case with first letter lower case, e.g. `someVariable`
 - Classes should use Camel Case but first letter upper case, e.g. `MyClass`
 - Package names should be all lower case, e.g. `helloworld`
 - Interfaces should be of form `ClassnameInterface`, e.g. `MyClassInterface`
 - Implementations of interfaces should be just the class/object name, e.g. `MyClass`
 - Unit test file names should be of form class name then "Test" appended, e.g. `ClassnameTest`
 - Individual test methods in a unit test file should start with "test" and follow with a brief description of the specific issue the test is testing, e.g. `testCreateNode()`

##5) Pull Dev before creating a pull request
After your code is complete, pull dev by using `git pull remote-name dev`. `remote-name` is the name of the main dev remote you set in 3). If there are no merge conflicts, go ahead and create a pull request. If there are merge conflicts, go onto dev github and see who committed the code that is conflicting. Contact them and resolve the issues together. Test building the project again after merge conflicts have been resolved, and then make a pull request.

##6) Creating a pull request
Verify your local version builds and all test cases pass. Go to the Github GUI and on the top left click "New pull request". Select Helen-Zhao/dev as the one to merge to and your local and the one you're merging from. Wait for the automatic TravisCI and merge conflict checks to pass. If there are failures, resolve them first. Otherwise, on the right hand side, under "Assignees", assign *two people* who did not work on the changes you are making a pull request for to code review the changes. It is the assignees responsibility to merge the pull request if they find it satisfactory.

##7) Code Reviewing someone else's code
Go to the pull request. Click "Files Changed" to see all the changes. Additions are green and removals are red. Go through the changes and comment on the pull request if there are any changes that should be made (need more test cases, variable names are confusing, System.out.println() left in the code, TODO comments that aren't removed). This will send a notification to the developer, who is responsible to make the changes suggested, or explain if there was a misunderstanding. When there are no issues with the code, check to see if there is a comment from the other assigned code reviewer saying they think it's all good. If there isn't, comment on the pull request that you think it's good. If you are the second reviewer and you think the code is good, merge the pull request ("Conversation > Merge Pull Request").

##8) You're done!