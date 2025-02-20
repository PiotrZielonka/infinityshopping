# 🛍️ InfinityShopping - E-commerce Shop Generator (Spring + Angular + JHipster)

## 🌍 Overview

InfinityShopping is a e-commerce shop generator built using Spring, Angular, and JHipster.

- 🌐 [**InfinityShopping Home Page**](https://www.infinityshopping.online)
- 🏪 [**Example Shop Website**](https://www.infinityshopping.online/example-shop)
- 📂 [**Example Shop GitHub Repository**](https://www.github.com/PiotrZielonka/infinityshopping-example-shop)
- 🎥 [**YouTube Demo**](https://www.youtube.com/watch?v=YYEodtIGeZQ)

## 🛠 Development Setup

### **1️⃣ Prerequisites

To run this project locally, do the following steps::

- ☕ **Java 11** (AdoptOpenJDK)
- 🔨 **Maven** (version 3.8.1)
- 🟢 **Node.js** ( version Fermium or 14.21.3) – [Download Here](https://nodejs.org/en/download)
- 📦 **npm** (version 7.24.2)
  ```sh
  npm install -g npm@7.24.2
  ```
- ⚡ **Angular CLI** (version 12.2.9)
  ```sh
  npm install -g @angular/cli@12.2.9
  ```
- 🚀 **JHipster CLI** (version 7.3.0)
  ```sh
  npm install -g generator-jhipster@7.3.0
  ```

> The versions above can be verified in the `pom.xml` and `package.json` files.

### **2️⃣ Recommended Tools

- **IDE for Java/Spring:** [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download)
- **IDE for Angular:** [Visual Studio Code](https://code.visualstudio.com)
- **IntelliJ Plugins:**
    - **MapStruct Support Plugin** → [Download](https://plugins.jetbrains.com/plugin/10036-mapstruct-support)

### **3️⃣ Building and Running the Application

1. Clone the repository and navigate to the project directory:
   ```
   git clone https://github.com/PiotrZielonka/infinityshopping.git
   ```

2. Build backend and frontend:
   ```sh
   mvnw
   ```
3. Now you can always start backend by running InfinityShoppingApp main method   
4. Once the backend is running, open a new terminal and start the frontend (Angular):
     ```sh
     npm start
     ```
5. Your application should now be up and running locally at http://localhost:9000
   InfinityShopping is a JHipster application, you can find more details in the  [JHipster home page](https://www.jhipster.tech).

## 🏗 Creating Your Own Shop

If you'd like develop your own shop, follow these steps:

1. Clone the repository and configure your remote repository:
   ```
   git remote -v
   git remote set-url origin <your-repository-url>
   git remote -v  # Verify the change
   ```
2. You should see your repository. You are on the branch master and run command below
   ```
   git push
   ```
3. Switch to the `develop` branch and push:
   ```sh
   git checkout develop
   git push
   ```
4. Verify that both `master` and `develop` branches are in your repository.

## 🧩 Add-ons & Customization

After setting up the project, you can extend it with additional features:

- 📦 Browse and install modules from the [**JHipster Marketplace**](https://www.jhipster.tech/modules/marketplace/#/list).
- ⚙️ Modify configurations in `yo-rc.json`.

## 🤝 Questions & Contributions

We welcome contributions! If you have any questions or would like to collaborate, visit [**www.infinityshopping.online**](https://www.infinityshopping.online) for contact information.



