# PlantAI Backend

This is the backend module of the PlantAI project.

## Prerequisites

In order to run the backend Java version 17 or higher must be installed on the system and have a valid API key for OpenAI, with access to the visual model of ChatGPT (gpt-4-vision-preview). For details on how to get an API key with access to the model, please see the API documentation of OpenAI: https://platform.openai.com/docs/overview

## Setup

The .env file contains variables for access to the OpenAI API, image settings and file hosting. Most variables should be left untouched, but the variables "GPT_API_KEY" and "LOCAL_IMAGE_DIR" need to be set.

* "GPT_API_KEY" should be set to your API key for OpenAI and will be used to make requests to the endpoint of the visual model.
* "LOCAL_IMAGE_DIR" should be set to a local directory on the backend machine, where received images will be saved.

## Endpoints

There are currently two endpoints in the backend:
* `GET localhost:3000/`: Endpoint to check if server is up and running. When the Server is started it automaticaly checks the validity of the provided API key and if it has access to the visual model.
* `POST localhost:3000/analysePlantImage`: Receives an image as multipart file and saves it to the path specified in LOCAL_IMAGE_DIR. The image cannot be larger IMAGE_MAX_WIDTH or IMAGE_MAX_HEIGHT, or it will be rejected. If the image meets the size criteria it is uploaded to the filehoster. After a successful upload, the url of the image is forwarded to the OpenAI api together with the prompt defined in ./main/resources/analysisPrompt.txt as system message. The prompt can be adjusted if necessary.
If a plant is shown in the image, it returns an analysis of its health status in the given form:
```
plant_name: [NAME_OF_THE_PLANT]  
health_status: ["healthy" OR "unhealthy"]  
disease_list: ["empty" OR LIST_OF_DISEASES]  
```
If the image does not show a plant, an according error message is returned.
