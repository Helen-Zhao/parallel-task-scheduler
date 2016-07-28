# softeng-306-p1

Git and Github cheat sheet!

##1) Forking the repository
On the Github GUI for github.com/Helen-Zhao/softeng-306-p1, click the "fork" button on the top right corner. Select your own account (i.e. instead of Helen-Zhao/softeng-306-p1 choose John-Doe/softeng-306-p1). Then, navigate to your newly forked directory on github (github.com/John-Doe/softeng-306-p1) and copy the "clone or download" link.  

##2) Cloning into the repo with SSH
Navigate to the folder you want to store your local copy of the repository. Right click, `Open Git BASH here`. If you're using SSH, simply type `git clone git@github.com:John-Doe/softeng-306-p1.git` into the terminal (the repository you're cloning into being the one that was just forked). You can create as many branches and stuff if you want

##3) Creating a pull request
Verify your local version builds and all test cases pass. Go to the Github GUI and on the top left click "New pull request". Select Helen-Zhao/dev as the one to merge to and your local and the one you're merging from. On the right hand side, under "Assignees", assign two people who did not work on the changes you are making a pull request for to code review the changes. It is the assignees responsibility to merge the pull request if they find it satisfactory.

##4) Code Reviewing someone else's code
Go to the pull request. Click "Files Changed" to see all the changes. Additions are green and removals are red. Go through the changes and comment on the pull request if there are any changes that should be made (need more test cases, variable names are confusing, System.out.println() left in the code, TODO comments that aren't removed). This will send a notification to the developer, who is responsible to make the changes suggested, or explain if there was a misunderstanding. When there are no issues with the code, merge the pull request ("Conversation > Merge Pull Request").
