#!/bin/bash

#for d in */ ; do
for d in Hafner*/ ; do
    echo "Patching code in directory $d"
    cd "$d"

    git status
    git checkout -b cleanup

    rm -f src/main/java/edu/hm/hafner/java/db/WarningTypeEntity.java
    rsync -a --prune-empty-dirs --include '*/' --include '*.java' --include '*.xml' --exclude '*' ~/git/assignment-dashboard/ ./
    cp -f ~/git/assignment-dashboard/.github/workflows/autograding.yml .github/workflows

    git add .
    git commit -a -m "Code cleanup and refactoring."
    git push --set-upstream origin cleanup

    gh pr create --head cleanup --base main --title "Cleanup und Refactoring" --body "Behebt einige Kleinigkeiten, so dass der main Branch keine Warnungen mehr enthält."

    git status

    cd ..
done

