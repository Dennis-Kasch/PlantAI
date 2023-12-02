import OpenAI from "openai";
import {} from 'dotenv/config'

const openai = new OpenAI({
    organization: process.env.ORGA_KEY
});

async function main() {
  const response = await openai.chat.completions.create({
    model: "gpt-4-vision-preview",
    messages: [
      {
        role: "user",
        content: [
          { type: "text", text: "What's in this image?" },
          {
            type: "image_url",
            image_url: {
              "url": "https://tierheim-wetzlar.de/wp-content/uploads/2019/08/IMG-20190907-WA0010-1023x1024.jpg",
              "detail": "low"
            },
          },
        ],
      },
    ],
  });
  console.log(response.choices[0]);
}

main();