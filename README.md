# PSIMiner

The tool for processing PSI trees from IntelliJ IDEA and creating labeled dataset from them.

## Usage

To run extracting data from a dataset with source code use this command
```shell
bash psiminer.sh dataset_path ouput_folder config.json 2> error_log.txt
```

Folder with [mock data](src/test/resources/mock_data) contains example of dataset.
[Config](config.json) shows the example of configuration.
To run this example use following command:
```shell
bash psiminer.sh psiminer-core/src/test/resources/mock_data output config.json 2> error_log.txt
```
