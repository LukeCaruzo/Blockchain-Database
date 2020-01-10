#!/bin/bash

array=(27017 27018 27019)
for i in "${array[@]}"; do
  mongo --port "$i" -u "admin" -p "test" <<EOF
admin = db.getSiblingDB("admin")
admin.shutdownServer();
EOF
done

kill "$(pgrep -f change_stream_daemon.sh)"