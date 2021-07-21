# Linux Cluster Monitoring Agent

# Introduction
The Linux Cluster Monitoring Agent manages a cluster of Linux nodes/hosts/servers that run on CentOS7. The connection between the hosts are enabled through a network switch and communication is accomplished via internal IPv4 addresses. This project provides a solution for the Linux Cluster Administration team to collect data on the hardware specifications of the hosts and monitor resource usages (CPU/memory) in real-time. Technologies used in implementing this program are Bash, Docker, PostgreSQL, and Git.


# Quick Start

Start a psql instance using psql_docker.sh (db_username = host_agent, db_password = password)
```
./scripts/psql_docker.sh create db_username db_password
```

Create tables using ddl.sql, on the host_agent database against the psql instance
```
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```

Insert hardware specs data into the DB using host_info.sh
```
./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
#sample: ./scripts/host_info.sh localhost 5432 host_agent postgres password
```

Insert hardware usage data into the DB using host_usage.sh
```
bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
#sample: ./scripts/host_usage.sh localhost 5432 host_agent postgres password
```
Crontab setup - edit crontab jobs
```
crontab -e
```
Collect usage data every minute - add to crontab
```
* * * * * bash <your path>/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password &< /tmp/host_usage.log
```
List crontab jobs
```
crontab -l
```

# Implementation
This project was implemented using Docker containers to provision a psql database instance that persists hardware and resource usage data, which is done through the **psql_docker** script. The **ddl.sql** script generates two tables, host_info and host_usage, where the data is stored and is used to perform data analytics. The two bash scripts **host_info.sh** and **host_usage.sh** collects data automatically and is installed on each server/node/host.

## Architecture
The architecture diagram for the Linux Monitory Agent program is shown below:

<img src="https://github.com/jarviscanada/jarvis_data_eng_KarinaShrestha/blob/release/linux_sql/assets/architecture_dg.png" width="850" height="650">

## Scripts
- **psql_docker.sh** is used to set up/start/stop a psql instance using docker
  ```
  #create a psql container with the given username and password
  ./scripts/psql_docker.sh create db_username db_password
  
  #start the psql docker container
  ./scripts/psql_docker.sh start
  
  #stop the psql docker container
  ./scripts/psql_docker.sh stop
  ```
- **ddl.sql** designs two tables to persist the hardware specifications data and resource usage data into the RDBMS database
  ```
  psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
  ```
  The monitoring agent is installed on every node/server/host to collect and persist data into the psql instance, which requires the following two scripts: host_info   and host_usage. The arguments are as follows:

    - ```psql_host``` : localhost
    - ``` psql_port ``` : 5432
    - ```psql_dbname``` : host_agent
    - ```psql_user``` : postgres
    - ```psql_password```: password

- **host_info.sh** collects the hardware specifications of the host and inserts it into the database.
  ```
  ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
  ```
- **host_usage.sh** collects the resource usage of the current host and inserts it into the database.
  ```
  bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
  ```
- **crontab** is used to trigger the host_usage script every minute to record data
  ```
  #edit cront tab jobs using vim
  crontab -e
  
  #collect usage data every minute (the path should be the full address of the scripts folder)
  * * * * * bash <your path>/linux_sql/scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password &< /tmp/host_usage.log
  #exit vim by pressing Esc and type :wq
  ```
- **queries.sql** answers a few business questions related to better managing the cluster and planning for future recourses.
  The three queries are:
    - group the hosts by their CPU number and sort their memory size in descending order, within each CPU number group
    - calculate the average used memory in percentage over a 5-minute interval for each host
    - detect host failure when less than 3 data points are inserted within a 5-minute interval

  ```
   psql -h localhost -U postgres -d host_agent -f sql/queries.sql
  ```

## Database Modeling
The database schema for the host_info and host_usage table are described below:
- `host_info`

| __Column Name__ | __Data Type__ | __Description__ |
| --------------- | :-------------: | --------------- |
| id | serial | auto-incremented PK, unique for each host |
| hostname | varchar | a fully qualified hostname |
| cpu_number | int | logical CPU number of a CPU |
| cpu_architecture | varchar | the architecture of the CPU |
| cpu_model | varchar | the model of the CPU |
| cpu_mhz | real | clock speed of the CPU (MHz) |
| L2_cache | int | level 2 cache (kB) | 
| total_mem | int | total RAM (kB) |
| timestamp | timestamp | time of data collection in UTC time zone |

- `host_usage`

| __Column Name__ | __Data Type__ | __Description__ |
| --------------- | :-------------: | --------------- |
| host_id | serial | FK, unique host id from the host_info table |
| timestamp | timestamp | time of data collection in UTC time zone |
| memory_free | int | amount of free RAM |
| cpu_idle | int | percentage of time the CPU is idle |
| cpu_kernel | int | percentage of CPU utilization at the kernel level |
| disk_io | int | number of disk I/O |
| disk_available | int | amount of available disk space (MB)

# Test
- The program was tested on a Linux host machine in which the bash scripts were tested manually by entering incorrect/invalid arguments to verify its functionality.
- A set of 10 sample test data were inserted into the database and tested on the PSQL command line to evaluate the SQL queries.

# Improvements
- Detect and handle hardware updates through a bash script
- Create a crontab script to perform the job instead of doing it manually
- Build a GUI to visualize the query results in a proper format
