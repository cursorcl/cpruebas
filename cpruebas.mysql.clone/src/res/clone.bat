mysql --defaults-extra-file=credentials.cnf --execute="create database %1"
mysqldump --defaults-extra-file=credentials.cnf --no-data cpruebas_base |  mysql --defaults-extra-file=credentials.cnf %1