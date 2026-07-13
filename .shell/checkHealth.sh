sudo apt-get install jq
export API_URL_SSM="`aws ssm get-parameter --name /prog4-exam-f5297b02/$1/api/url`"
export API_URL=`echo $API_URL_SSM | jq -r '.Parameter.Value'`
curl --fail "$API_URL$2"