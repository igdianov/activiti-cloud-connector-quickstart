# activiti-cloud-connector-quickstart

Activiti Cloud Connector Quickstart for Jenkins-X CI/CD pipelines

### How to use

You will need to use Jx Quickstart commands to create our own Activiti Cloud Connector from published quickstart templates in https://github/activiti organization repository

Run this command to add Activiti Quickstart location for your team:

```
jx create quickstartlocation --url https://github.com --owner activiti --kind github
```

To get the list of registered quickstart locations run command:

```
jx get quickstartlocations
```

Then, run simply run the following command to create your first runtime bundle or connector project:

```
jx create quickstart --owner activiti --filter activiti-cloud-connector-quickstart
```
