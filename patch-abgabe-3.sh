#!/bin/bash

#for d in */ ; do
for d in dashboard-hackl*/ ; do
    echo "Patching code in directory $d"
    cd "$d"

    git status

#    firstRev=`git rev-list --max-parents=0 HEAD`
#    git checkout -b clean-main $firstRev
#    git push --set-upstream origin clean-main
#    git checkout -b cleanup
#
#    rm -f src/main/java/edu/hm/hafner/java/db/WarningTypeEntity.java
    rsync -a --prune-empty-dirs --include '*/' --include '*.java' --include '*.xml' --exclude '*' '/Users/hafner/git/Static Analysis Dashboard-06-16-2022-06-17-47/Njah-Hackl/' ./

    git add .
    git commit -a -m "Abgabe  3 wiederhergestellt."
    git push --set-upstream origin abgabe-3

    gh pr create --head abgabe-3 --base main --title "Abgabe 3" --body "Abgabe  3 wiederhergestellt."

    git status

    cd ..
done

