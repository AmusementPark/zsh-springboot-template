-- INIT SQL
CREATE USER 'zsh'@'%' IDENTIFIED BY 'aq1sw2de';
GRANT ALL PRIVILEGES ON *.* TO 'zsh'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;