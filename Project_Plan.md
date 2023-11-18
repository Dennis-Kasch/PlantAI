# PlantAI - Project Plan

This is the project plan for the PlantAI project for Systems and Software Engineering I - WS 23/24, at Goethe University, Frankfurt.

## Group Members

* Abdelkhalek Kamkoum (6331906)
* Ayham Ammar
* Daniel Hahn
* David Jordan
* Dennis Kasch
* Sarahe El Mimouni
* Shivan Oskan

Competences: web development, working with databases, java and python programming, basics of creating ML models  
Missing competences: implementing Android apps, creating computer vision models

## Project Goals

The goal of this project is to develop an Android app that allows to take pictures of a single plant, perform a visual analysis of it and return an assessment of the plant's health status. Corn plants were chosen as a starting point, because they play an important part in agriculture and good datasets exists for it. In the first stage the app should be able to distinguish between healthy or unhealthy plants. But the assessment will eventually add "water level" analysis, nutrition of the plant and/or possible diseases. For the possible diseases the 3 most common of the selected plant type should be identified.

## Software Architecture

* Android Basis + UI
* Models and Algorithms (Visual Analysis)
* Data Management

## Responsibilities

* **Android development:** 
* **Models and Algorithms**: Dennis, David
* **Data Management:**
* **Testing:**
* **Documentation:**

## Schedule

* **Research Phase (1-2 weeks):** Figure out what data to use, how/if to implement on Android or web, what technology to use and for what plant.
* **Implementation phase I (3-4 weeks):** Implement a basic UI that allows to take pictures and receive an assessment that shows if the plant is healthy or not, maybe also water level and/or nutrition.
* **Implementation phase II (3-4 weeks):** Optimize UI and refine assessment to more complexity (nutrition, diseases) . Could possible include further plants when phase I is going well.
* **Delivery phase (1-2 weeks):** Further optimization and bug fixing. Create a final product.

## Risks, Challenges and Counter Measures

* **Availability of data:** Selection of a fitting plant (corn), automated data generation, simulation
* **Sickness:** Duos or (partly) overlapping so there are always two people responsible for a topic, highly depends on final team size
* **Time:** Gradual increments of the app (general health, water level, plant nutrition, diseases, amount of plants)
