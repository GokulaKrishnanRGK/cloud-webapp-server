#Update Packages
sudo yum update -y

sudo yum upgrade -y


#Install Java 17
sudo rpm --import https://yum.corretto.aws/corretto.key
sudo curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo
sudo yum install -y java-17-amazon-corretto-devel

export JAVA_HOME="/usr/lib/jvm/java-17-amazon-corretto"
export PATH=$PATH:$JAVA_HOME/bin

#Install MySQL
sudo yum install mariadb mariadb-server -y

sudo systemctl start mariadb

sudo systemctl enable mariadb

sudo mysqladmin -u root password "root"

mysqladmin -u root --password=root --host=localhost --port=3306 create csye6225

#Start Java Application
sudo systemctl daemon-reload

sudo systemctl start webapp.service

sudo systemctl enable webapp.service