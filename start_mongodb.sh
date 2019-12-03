#!/bin/sh

mkdir -p ./data

mongod --auth --dbpath ./data --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork

ps aux | grep -v grep | grep mongod
