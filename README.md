The programm will take the specific files in the folder "dataFiles/" and persist the data in a PostgreSQL database.
It will then fetch the data and generate a JPEG and a PDF for a Pie Chart and a Ring Chart.
These files will be in a folder named "generatedFiles/".
Output will be logged in Log.log in the home path.
generatedFiles/ and the log will be cleaned at the start of the next execution.

This exercise was build with Spring Data JPA using Hibernate. 
CSV parsing was done with Apache Commons CSV.
XLS parsing was done using Apache POI HSSF.
Ring and Pie Charts were created with JFreeChart.
PDF export was done with iText.
Logging is done with slf4j and Logback.
