--Group hosts by CPU number and sort by their memory size in descending order
SELECT cpu_number, host_id, total_mem
FROM
     ( SELECT cpu_number, host_id, total_mem
         RANK () OVER (
            PARTITION BY cpu_number
            ORDER BY
                total_mem DESC
         ) as sortedmem
       FROM PUBLIC.host_info
       GROUP BY cpu_number, sortedmem
    ) as  group_memsize;

--insert some sample data points for testing purposes
INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('2021-07-10 15:00:00.000', 1, 300000, 90, 4, 3, 2);
INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('2021-07-10 15:01:00.000', 1, 300040, 90, 4, 3, 2);
INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('2021-07-10 15:02:00.000', 1, 301000, 90, 3, 3, 2);
INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('2021-07-10 15:03:00.000', 1, 200000, 90, 4, 3, 2);

--round current ts every 5 min
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
    $$
    BEGIN
        RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;

--Average used memory in % over 5 min interval for each host (used memory = total-free)
SELECT host_id, timestamp, round5(timestamp),
  AVG((total_mem - memory_free) * 100 / total_mem) AS avg_used_mem_percentage,
  FROM host_usage
  INNER JOIN host_info ON host_info.id=host_usage.id
GROUP BY host_id, host_name, timestamp, avg_used_mem_percentage
ORDER BY host_id;

-- detect host failures - less than 3 data points exist within a 5 min interval
SELECT host_id, timestamp, round5(timestamp) COUNT(*) as num_data_points
FROM host_usage
GROUP BY host_id, timestamp, round5(timestamp)
HAVING COUNT(*) < 3
ORDER BY host_id;