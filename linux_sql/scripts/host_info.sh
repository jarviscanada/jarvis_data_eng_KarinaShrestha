#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5
# create environment variable
export PGPASSWORD="$psql_password"

if [ "$#" -ne 5 ]; then
  echo "Error: invalid or missing parameters"
  echo "View correct usage: scripts/host_info.sh psql_host psql_port db_name psql_user psql_password"
  exit 1
fi

#save command outputs to a variable
hostname=$(hostname -f)
lscpu_out=`lscpu`
mem_info=$(cat /proc/meminfo)

#parse server CPU and memory usage using bash scripts
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
#$1=$2=";print $0 -> print all but the first 2 columns
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{$1=$2=""; print $0}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
L2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(echo "$mem_info" | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date + "%F %T")

#insert data into PSQL from bash script
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp)
VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, '$L2_cache', '$total_mem', '$timestamp');"

psql -h $psql_host -U $psql_user -d $db_name -p $psql_port -c "$insert_stmt"
exit $?