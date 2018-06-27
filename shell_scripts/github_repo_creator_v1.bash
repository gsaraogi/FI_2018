#!/bin/bash


create_new_repo()
{
if [[ ! -d $repo_path/$reponame ]];
then
mkdir $repo_path/$reponame
fi

cd $repo_path/$reponame
curl --insecure -i -u $user:$pass https://api.github.com/user/repos -d "{\"name\":\"$reponame\"}"
git init
git add .
git commit -m "Updating repository: $reponame on `date`"
git remote add origin git@github.com:$user/$reponame.git
git push -u origin master
}

push_repo()
{
git ls-remote "$repourl" HEAD -q
if [[ $? -eq 0 ]];
then
cd $repo_path
git pull
git add .
git commit -m "Updating repository: $repourl on `date`"
git push -u origin master
else
echo "ERROR: $repourl not found in GitHub.Exiting..."
exit 1
fi
}


read -p "Enter 1 for New Git Repository 2 for Existing Git Repository: " choice
if [ "$choice" = 1 ]; then
read -p "Enter Github Repository Name: " reponame
read -p "Enter Github User Name: " user
read -p "Enter Github Password: " pass
read -p "Enter Local Repository Path: " repo_path
create_new_repo $reponame $user $pass $repo_path
elif [ "$choice" = 2 ]; then
read -p "Enter Github Repository URL: " repourl
read -p "Enter Local Repository Path: " repo_path
push_repo $repourl $repo_path
else
echo "Wrong choice.Try again..."
exit 2
fi
