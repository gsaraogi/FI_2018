#!/bin/bash
export reponame="$1"
export user="$2"
export pass="$3"
export repo_path="$4"

if [ "$reponame" = "" ]; then
read -p "Enter Github Repository Name: " reponame
fi
if [ "$user" = "" ]; then
read -p "Enter Github Repository Name: " user
fi
if [ "$pass" = "" ]; then
read -p "Enter Github Repository Name: " pass
fi
if [ "$repo_path" = "" ]; then
read -p "Enter Github Repository Name: " repo_path
fi

mkdir $repo_path/$reponame
cd $repo_path/$reponame
curl --insecure -i -u $user:$pass https://api.github.com/user/repos -d "{\"name\":\"$reponame\"}"
git init
git add .
git commit -m "Updating repository: $reponame on `date`"
git remote add origin git@github.com:$user/$reponame.git
git push -u origin master