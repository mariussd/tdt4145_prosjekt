#!/bin/bash

echo "Loading tables and data to db"

for f in dev/sql/*
do
echo "$f"
    mysql -P 3306 -h 127.0.0.1 -u root -p$MYSQL_ROOT_PASSWORD $MYSQL_DATABASE < $f
done
