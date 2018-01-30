[![Build Status](https://travis-ci.org/peavers/cwp-backup-service.svg?branch=master)](https://travis-ci.org/peavers/cwp-backup-service)
[![Maintainability](https://api.codeclimate.com/v1/badges/789cf79cd2da3b6fda99/maintainability)](https://codeclimate.com/github/peavers/cwp-backup-service/maintainability)
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/peavers/cwp-backup-service/blob/master/LICENSE)

# CWP Backup Service 

**Add nightly snapshots to AWS S3 for easier developer access**

While Dash does a good job at taking the snapshots, it's incredibly slow to download from and requires highly privileged users to create
production snapshots. This project aims to ease those pain points by sending nightly snapshots to a S3 bucket, and providing a presigned
URL to a Slack channel with the download link.

### Silverstripe Client
Deals with tasks and cron triggers for preforming actions.

### Silverstripe Service
Communicates to Dash via authenticated REST api.

The following are required for basic usage, set in the docker-compose file.
* `SLACK_TOKEN` - Bot authentication token, this is used for sending download links to slack.
* `SLACK_CHANNEL` - Slack channel name for notification links.
* `SILVERSTRIPE_USERNAME` - Dash login name, normally a email address.
* `SILVERSTRIPE_TOKEN` - Dash authentication token. Needs to have permission to create/delete production assets and databases
* `AWS_BUCKET` - Bucket to upload assets/databases too. 

#### Run with docker-compose

```
docker-compose up -d
```