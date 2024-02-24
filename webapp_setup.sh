#Update Packages
sudo yum update -y

sudo yum upgrade -y

#Install Java 17
sudo rpm --import https://yum.corretto.aws/corretto.key
sudo curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo
sudo yum install -y java-17-amazon-corretto-devel

export JAVA_HOME="/usr/lib/jvm/java-17-amazon-corretto"
export PATH=$PATH:$JAVA_HOME/bin