# queryclient
A client to send queries to a query processing node

## Usage
The query client receives in input a textual file, containing a query per line.

ConstantRateQueryClient sends queries at a constant rate. The query file is processed line by line, and each line has to contain query_id and query, tab separated.

Usage: java QueryClient <server host name> <port number> <query file> <send rate>

QueryLogQueryClient sends queries using interarrival times. The query file is processed line by line. Each line has to contain query_id, query_time and query, tab separated. query_time is the interval from the origin in ms.

Usage: java QueryLogQueryClient <server host name> <port number> <query file>