# FAIR model commissioning station

This repository contains a FAIR prediction model cache (from the [fairmodels.org](fairmodels.org) index). This application makes the distributed index searcheable, with instructions how to execute this model. Hence, representing the **F**indable and **A**ccessible aspects of the FAIR principles.

## Running the station

The station can be executed by running the following docker command.
```
docker run \
    -p 8080:8080 \
    registry.gitlab.com/um-cds/projects/faircomml/model-commissioning-station
```

Afterwards, you can open the following local webpage [http://localhost:8080](http://localhost:8080).
