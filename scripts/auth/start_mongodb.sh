#!/bin/bash

mongod --dbpath ../../data --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --auth
