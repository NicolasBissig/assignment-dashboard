#!/bin/bash

for d in */ ; do
#for d in Hafner*/ ; do
    echo "Use SSH as auth strategy in remote URL of assignments $d"
    cd "$d"

    url=`git remote get-url origin | sed 's|.*github.com/||'`
    sshUrl="git@github.com:$url"
    echo "Changing URL to $sshUrl"
    git remote set-url origin $sshUrl
    git pull

    git status

    cd ..
done

