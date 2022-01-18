cd coms-ui
docker build -t registry.gitlab.com/jucoms/coms/coms-ui .
docker push registry.gitlab.com/jucoms/coms/coms-ui 

cd ..

cd coms-customer-api
docker build -t registry.gitlab.com/jucoms/coms/coms-customer-api .
docker push registry.gitlab.com/jucoms/coms/coms-customer-api
