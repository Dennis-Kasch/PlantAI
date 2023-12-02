import OpenAI from "openai";
import {} from 'dotenv/config';
import express from "express";

const app = express ();
const PORT = process.env.PORT
const openai = new OpenAI({
    organization: process.env.ORGA_KEY
});

async function getGptDescription(imageUrl) {
    const response = await openai.chat.completions.create({
        model: "gpt-4-vision-preview",
        messages: [{
            role: "user",
            content: [
            { type: "text", text: "What's in this image?" },
            {
                type: "image_url",
                image_url: {
                "url": imageUrl,
                "detail": "low"
                },
            },],
        },],
        max_tokens: 100
    });
    return response.choices[0]
}

app.use(express.json());

app.listen(PORT, () => {
    console.log("Server listening on PORT:", PORT);
});

app.get("/status", (request, response) => {
    response.send("Server is running on port "+PORT);
});

app.post('/describeUrlImage', async (request, response) => {
    console.log("[REQUEST] imageUrl="+request.body.imageUrl)
    let imageDescription = await getGptDescription(request.body.imageUrl);
    console.log("[GPT-4 RESPONSE] "+imageDescription.message.content)
    response.send(imageDescription.message.content)
})

app.post('/describeImageFile', (request, response) => {
    response.status(501)
    response.send("Sorry, but i can't handle image files yet. Please use /describeUrlImage instead.")
})