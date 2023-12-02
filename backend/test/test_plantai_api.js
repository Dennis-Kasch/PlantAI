fetch("http://localhost:3000/describeUrlImage", {
  method: "POST",
  body: JSON.stringify({
    imageUrl: "https://tierheim-wetzlar.de/wp-content/uploads/2019/08/IMG-20190907-WA0010-1023x1024.jpg"
  }),
  headers: {
    "Accept": "application/html",
    "Content-type": "application/json; charset=UTF-8"
  }
})
.then((response) => response.text())
.then((text) => console.log(text));