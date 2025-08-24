🖊️ Text to Handwriting Generator

A Java Swing + iText7 based desktop application that converts typed text into a handwritten-style document on a textured paper background.
The generated output can be previewed, saved as an image (.png), and exported as a properly scaled A4 PDF.

✨ Features
* 📝 Text Input – Type or paste any text.
* 🎨 Handwriting Fonts – Choose from multiple custom handwriting fonts (.ttf / .otf).
* 📏 Custom Font Size – Adjust handwriting size dynamically.
* 📄 Paper Textures – Apply realistic notebook/paper backgrounds.
* 🖌️ Pen Color Selection – Choose any ink color with a color picker.
* 🤖 Humanized Writing Effect – Slight random rotations, spacing, and jitter make the handwriting look natural.
* 👀 Preview Window – See the generated handwriting before saving.
* 💾 Save Options:
  
   Export as handwriting.png image.

   Export as handwriting.pdf with A4 scaling using iText7.

🛠️ Tech Stack
* Java Swing – GUI development
* AWT & Graphics2D – Rendering handwriting
* iText7 – PDF export
* Custom Fonts – Handwriting simulation
* Paper Textures – Realistic notebook backgrounds 

📂 Project Structure

    /fonts       → Handwriting fonts (.ttf / .otf)
    /textures    → Paper background images (.png)
    /src         → Java source code

🚀 How to Run
1. Clone this repo
2. Place handwriting fonts in the /fonts folder
3. Place paper textures in the /textures folder
4. Compile and run:

javac -cp "itext7-core.jar;slf4j-api.jar;slf4j-simple.jar" TextToHandwritingGUI.java

java -cp ".;itext7-core.jar;slf4j-api.jar;slf4j-simple.jar" TextToHandwritingGUI



👉 This project is useful for creating personalized notes, assignments, and digital handwriting documents in a fun and realistic way.
