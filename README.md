# unit-test-generator

Gradle version - 6.5

1) run "gradle fatJar"
2) run "docker container prune"
3) run "docker run -v /home/path/to/project/name/:/home/name --name testgen -it dockercontainervm/junitcontest:test"
5) run "cd home/test-generator"
4) run "./runGeneration"

the stats are in ./stats_log.txt