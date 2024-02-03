# CookingAgent
This is the code for a conversational agent whose name is "CookingAgent".

## Introduction
The cooking agent can provide the user with fitting recipes. The agent will first ask the user some questions like "What kind of food do you like?", "How many people will eat the meal?", etc. It will then use these information and the knowledge in past interactions to give a recommendation, which contains the name of the dish, ingredients and their quantity, instructions about how to cook the dish, etc.

## Usage

### Configure environment
* Furhat SDK
* OpenJDK
* IntelliJ IDEA

### Set OpenAI service key
In [this file](src/main/kotlin/furhatos/app/cookingagent/setting/interactionParams.kt), set the value of SERVICE_KEY to your own service key.
(You can generate your key from [here](https://openai.com/api/).)
```code
const val SERVICE_KEY = "YOUR_SERVICE_KEY"
```
### Run the program
About how to run the program, see [this tutorial](https://docs.furhat.io/skills/).
