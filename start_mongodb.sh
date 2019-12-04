#!/bin/sh

mkdir -p ./data

mongod --dbpath ./data --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --auth

ps aux | grep -v grep | grep mongod
