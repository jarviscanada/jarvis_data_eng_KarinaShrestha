#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5
export PGPASSWORD="$psql_password"

if [ "$#" -ne 5 ]; then
  echo "Error: invalid or missing parameters"
  echo "View correct usage: scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password"
  exit 1
fi

#save command outputs to a variable
hostname=$(hostname -f)
mem_info=$(cat /proc/meminfo)
cpu_time=$(vmstat -t)
disk_info=$(vmstat -d)
disk_space=$(df -BM /)

timestamp=$(date + "%F %T")
memory_free=$(echo "$mem_info" | egrep "^MemFree:" | awk '{print $2}' | xargs)
cpu_idle=$(echo "$cpu_time" | awk '{if (NR==3) print $15"%"}')
cpu_kernel=$(echo "$cpu_time" | awk '{if (NR==3) print $14"%"}')
disk_io=$(echo "$disk_info" | egrep "sda" | awk '{print $10}' | xargs)
disk_available=$(echo "$disk_space" | egrep "/dev/sda2" | awk '{print $4}' | xargs)

insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES ('$timestamp', (SELECT id FROM host_info WHERE hostname='$hostname'),'$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

psql -h $psql_host -U $psql_user -d $db_name -p $psql_port -c "$insert_stmt"
exit $?