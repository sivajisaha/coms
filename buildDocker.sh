docker login -u sibendu -p P@ssw0rd12
cd coms-ui
docker build -t sibendu/coms-ui .
docker push sibendu/coms-ui 

cd coms-customer-api
docker build -t sibendu/coms-customer-api .
docker push sibendu/coms-customer-api
