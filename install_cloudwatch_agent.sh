sudo yum install amazon-cloudwatch-agent -y

sudo mkdir -p /opt/aws/amazon-cloudwatch-agent/etc
sudo mv /tmp/cloudwatch_config.json /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json
sudo chmod 644 /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json