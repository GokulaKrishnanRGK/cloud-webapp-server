curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install

cat <<EOF | sudo tee /etc/google-cloud-ops-agent/config.yaml
logging:
  receivers:
    webapp-receiver:
      type: files
      include_paths:
      - /var/log/csye6225/*.log
      record_log_file_path: true
  processors:
    extract_time:
      type: parse_json
      time_key: timestamp
      time_format: "%Y-%m-%dT%H:%M:%S.%L%z"
    move_severity:
      type: modify_fields
      fields:
        severity:
          move_from: jsonPayload.level
  service:
    pipelines:
      default_pipeline:
        receivers: [webapp-receiver]
        processors: [extract_time, move_severity]
EOF

# Restart Ops Agent
sudo systemctl enable google-cloud-ops-agent
sudo systemctl start google-cloud-ops-agent
