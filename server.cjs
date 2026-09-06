var __create = Object.create;
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __getProtoOf = Object.getPrototypeOf;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
  // If the importer is in node compatibility mode or this is not an ESM
  // file that has been converted to a CommonJS file using a Babel-
  // compatible transform (i.e. "__esModule" has not been set), then set
  // "default" to the CommonJS "module.exports" for node compatibility.
  isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
  mod
));

// server.ts
var import_express = __toESM(require("express"), 1);
var import_path = __toESM(require("path"), 1);
var import_fs = __toESM(require("fs"), 1);
var import_vite = require("vite");
var import_genai = require("@google/genai");
var import_dotenv = __toESM(require("dotenv"), 1);
import_dotenv.default.config();
async function startServer() {
  const app = (0, import_express.default)();
  const PORT = process.env.PORT ? parseInt(process.env.PORT, 10) : 3e3;
  app.use(import_express.default.json());
  const apiKey = process.env.GEMINI_API_KEY;
  let aiClient = null;
  if (apiKey) {
    aiClient = new import_genai.GoogleGenAI({
      apiKey,
      httpOptions: {
        headers: {
          "User-Agent": "aistudio-build"
        }
      }
    });
  }
  app.get("/api/health", (req, res) => {
    res.json({ status: "ok", aiEnabled: !!aiClient });
  });
  app.post("/api/ai-thimar", async (req, res) => {
    try {
      if (!aiClient) {
        return res.status(503).json({ error: "Gemini API key not configured on server" });
      }
      const { topic, era } = req.body || {};
      const prompt = `\u0623\u0646\u062A \u0639\u0627\u0644\u0645 \u0645\u062D\u0642\u0642 \u0628\u0627\u0644\u0642\u0631\u0622\u0646 \u0627\u0644\u0643\u0631\u064A\u0645 \u0648\u0627\u0644\u0633\u0646\u0629 \u0627\u0644\u0646\u0628\u0648\u064A\u0629 \u0648\u062F\u0631\u0631 \u0639\u0644\u0645\u0627\u0621 \u0623\u0647\u0644 \u0627\u0644\u0633\u0646\u0629. 
\u0627\u0644\u0645\u0637\u0644\u0648\u0628: \u0642\u0645 \u0628\u062A\u0648\u0644\u064A\u062F \u062B\u0645\u0631\u0629 \u0639\u0644\u0645\u064A\u0629 \u062C\u062F\u064A\u062F\u0629 \u0648\u0645\u0648\u062B\u0648\u0642\u0629 \u0645\u0646 \u0623\u0642\u0648\u0627\u0644 \u0648\u0623\u0639\u0644\u0627\u0645 \u0623\u0647\u0644 \u0627\u0644\u0633\u0646\u0629 \u0648\u0627\u0644\u062C\u0645\u0627\u0639\u0629 (\u0645\u062B\u0644: \u0627\u0644\u062D\u0633\u0646 \u0627\u0644\u0628\u0635\u0631\u064A\u060C \u0633\u0641\u064A\u0627\u0646 \u0627\u0644\u062B\u0648\u0631\u064A\u060C \u0627\u0628\u0646 \u062A\u064A\u0645\u064A\u0629\u060C \u0627\u0628\u0646 \u0627\u0644\u0642\u064A\u0645\u060C \u0627\u0628\u0646 \u0631\u062C\u0628\u060C \u0627\u0644\u062D\u0627\u0641\u0638 \u0627\u0628\u0646 \u0627\u0644\u062C\u0648\u0632\u064A\u060C \u0623\u0648 \u0627\u0644\u0634\u064A\u062E \u0623\u0628\u0648 \u0625\u0633\u062D\u0627\u0642 \u0627\u0644\u062D\u0648\u064A\u0646\u064A\u060C \u0627\u0644\u0634\u064A\u062E \u0627\u0628\u0646 \u0639\u062B\u064A\u0645\u064A\u0646\u060C \u0627\u0644\u0634\u064A\u062E \u0627\u0644\u0623\u0644\u0628\u0627\u0646\u064A\u060C \u0627\u0644\u0634\u064A\u062E \u0627\u0628\u0646 \u0628\u0627\u0632\u060C \u0627\u0644\u0634\u064A\u062E \u0639\u0628\u062F \u0627\u0644\u0631\u0632\u0627\u0642 \u0627\u0644\u0628\u062F\u0631).
${topic ? `\u0627\u0644\u0645\u0648\u0636\u0648\u0639 \u0627\u0644\u0645\u0637\u0644\u0648\u0628: ${topic}` : ""}
${era ? `\u0627\u0644\u0639\u0635\u0631 \u0627\u0644\u0645\u0637\u0644\u0648\u0628: ${era}` : ""}

\u0627\u0644\u0645\u0637\u0644\u0648\u0628 \u0625\u0631\u062C\u0627\u0639\u0647 \u0628\u0635\u064A\u063A\u0629 JSON \u0641\u0642\u0637 \u0628\u0647\u0630\u0627 \u0627\u0644\u0634\u0643\u0644 \u0648\u0628\u0635\u064A\u063A\u0629 \u0635\u062D\u064A\u062D\u0629 \u0628\u062F\u0648\u0646 markdown tags:
{
  "quote": "\u0627\u0644\u0645\u0642\u0648\u0644\u0629 \u0623\u0648 \u0627\u0644\u062F\u0631\u0629 \u0627\u0644\u0639\u0644\u0645\u064A\u0629 \u0627\u0644\u0645\u062D\u0642\u0642\u0629",
  "author": "\u0627\u0633\u0645 \u0627\u0644\u0634\u064A\u062E \u0623\u0648 \u0627\u0644\u0625\u0645\u0627\u0645",
  "category": "\u062A\u0635\u0646\u064A\u0641 \u0627\u0644\u0641\u0627\u0626\u062F\u0629",
  "source": "\u0627\u0644\u0645\u0635\u062F\u0631 \u0627\u0644\u0639\u0644\u0645\u064A \u0623\u0648 \u0627\u0644\u0643\u062A\u0627\u0628",
  "reflectionPrompt": "\u0648\u0642\u0641\u0629 \u062A\u062F\u0628\u0631\u064A\u0629 \u0645\u0648\u062C\u0632\u0629 \u0645\u0639 \u0627\u0644\u0641\u0627\u0626\u062F\u0629",
  "era": "\u0625\u0645\u0627 '\u0627\u0644\u062A\u0627\u0628\u0639\u064A\u0646 \u0648\u0627\u0644\u0623\u0626\u0645\u0629 \u0627\u0644\u0645\u062A\u0642\u062F\u0645\u064A\u0646' \u0623\u0648 '\u0623\u0626\u0645\u0629 \u0627\u0644\u0625\u0633\u0644\u0627\u0645 \u0627\u0644\u0645\u062D\u0642\u0642\u064A\u0646' \u0623\u0648 '\u0645\u0634\u0627\u064A\u062E \u0627\u0644\u0639\u0635\u0631 \u0648\u0623\u0639\u0644\u0627\u0645 \u0627\u0644\u0633\u0646\u0629'",
  "relatedVerse": {
    "surahName": "\u0627\u0633\u0645 \u0627\u0644\u0633\u0648\u0631\u0629",
    "surahNumber": 1,
    "ayahNumber": 1,
    "verseText": "\u0646\u0635 \u0627\u0644\u0622\u064A\u0629 \u0627\u0644\u0642\u0631\u0622\u0646\u064A\u0629 \u0627\u0644\u0643\u0631\u064A\u0645\u0629 \u0627\u0644\u062F\u0627\u0644\u0629 \u0648\u0627\u0644\u0645\u0642\u062A\u0631\u0646\u0629 \u0628\u0627\u0644\u0645\u0639\u0646\u0649",
    "explanation": "\u0648\u062C\u0647 \u0627\u0644\u0627\u0631\u062A\u0628\u0627\u0637 \u0627\u0644\u0634\u0631\u064A\u0641 \u0628\u064A\u0646 \u0647\u0630\u0647 \u0627\u0644\u0622\u064A\u0629 \u0627\u0644\u0643\u0631\u064A\u0645\u0629 \u0648\u062A\u0644\u0643 \u0627\u0644\u0641\u0627\u0626\u062F\u0629 \u0627\u0644\u0639\u0644\u0645\u064A\u0629 \u0623\u0648 \u0627\u0644\u0630\u0643\u0631",
    "audioUrl": "\u0631\u0627\u0628\u0637 \u0635\u0648\u062A\u064A \u0628\u0635\u064A\u063A\u0629 https://everyayah.com/data/Alafasy_128kbps/SSSAAA.mp3 \u062D\u064A\u062B SSS \u0631\u0642\u0645 \u0627\u0644\u0633\u0648\u0631\u0629 3 \u062E\u0627\u0646\u0627\u062A \u0648 AAA \u0631\u0642\u0645 \u0627\u0644\u0622\u064A\u0629 3 \u062E\u0627\u0646\u0627\u062A"
  }
}`;
      const response = await aiClient.models.generateContent({
        model: "gemini-3.7-flash",
        contents: prompt,
        config: {
          responseMimeType: "application/json",
          temperature: 0.7
        }
      });
      const text = response.text || "";
      const cleaned = text.replace(/```json/g, "").replace(/```/g, "").trim();
      const parsed = JSON.parse(cleaned);
      return res.json({ success: true, data: parsed });
    } catch (err) {
      console.error("Error in /api/ai-thimar:", err);
      return res.status(500).json({ error: err.message || "Failed to generate AI thimar" });
    }
  });
  app.post("/api/ai-quran-link", async (req, res) => {
    try {
      if (!aiClient) {
        return res.status(503).json({ error: "Gemini API key not configured on server" });
      }
      const { textToLink } = req.body || {};
      if (!textToLink) {
        return res.status(400).json({ error: "Missing textToLink parameter" });
      }
      const prompt = `\u0627\u0633\u062A\u062E\u0631\u062C \u0622\u064A\u0629 \u0642\u0631\u0622\u0646\u064A\u0629 \u0643\u0631\u064A\u0645\u0629 \u062F\u0627\u0644\u0629 \u0648\u0645\u0642\u062A\u0631\u0646\u0629 \u0645\u0628\u0627\u0634\u0631\u0629 \u0628\u0647\u0630\u0627 \u0627\u0644\u0630\u0643\u0631 \u0623\u0648 \u0627\u0644\u0645\u0642\u0648\u0644\u0629 \u0627\u0644\u0625\u064A\u0645\u0627\u0646\u064A\u0629:
"${textToLink}"

\u0642\u0645 \u0628\u0625\u0631\u062C\u0627\u0639 \u0627\u0644\u0646\u062A\u064A\u062C\u0629 \u0628\u0635\u064A\u063A\u0629 JSON \u0641\u0642\u0637:
{
  "surahName": "\u0627\u0633\u0645 \u0627\u0644\u0633\u0648\u0631\u0629",
  "surahNumber": 1,
  "ayahNumber": 1,
  "verseText": "\u0646\u0635 \u0627\u0644\u0622\u064A\u0629 \u0627\u0644\u0643\u0631\u064A\u0645\u0629 \u0627\u0644\u0648\u0627\u0636\u062D\u0629 \u0627\u0644\u062F\u0627\u0644\u0629 \u0639\u0644\u0649 \u0647\u0630\u0627 \u0627\u0644\u0645\u0639\u0646\u0649",
  "explanation": "\u0634\u0631\u062D \u0645\u0648\u062C\u0632 \u0644\u0628\u0644\u064A\u063A \u0627\u0644\u0631\u0628\u0637 \u0628\u064A\u0646 \u0627\u0644\u0630\u0643\u0631/\u0627\u0644\u0641\u0627\u0626\u062F\u0629 \u0648\u0647\u0630\u0647 \u0627\u0644\u0622\u064A\u0629 \u0627\u0644\u0645\u0628\u0627\u0631\u0643\u0629",
  "audioUrl": "\u0631\u0627\u0628\u0637 \u0635\u0648\u062A\u064A \u0628\u0635\u064A\u063A\u0629 https://everyayah.com/data/Alafasy_128kbps/SSSAAA.mp3 \u062D\u064A\u062B SSS \u0631\u0642\u0645 \u0627\u0644\u0633\u0648\u0631\u0629 3 \u062E\u0627\u0646\u0627\u062A \u0648 AAA \u0631\u0642\u0645 \u0627\u0644\u0622\u064A\u0629 3 \u062E\u0627\u0646\u0627\u062A"
}`;
      const response = await aiClient.models.generateContent({
        model: "gemini-3.7-flash",
        contents: prompt,
        config: {
          responseMimeType: "application/json",
          temperature: 0.5
        }
      });
      const text = response.text || "";
      const cleaned = text.replace(/```json/g, "").replace(/```/g, "").trim();
      const parsed = JSON.parse(cleaned);
      return res.json({ success: true, data: parsed });
    } catch (err) {
      console.error("Error in /api/ai-quran-link:", err);
      return res.status(500).json({ error: err.message || "Failed to link Quran verse" });
    }
  });
  const distPath = import_path.default.join(process.cwd(), "dist");
  const hasDist = import_fs.default.existsSync(import_path.default.join(distPath, "index.html"));
  if (process.env.NODE_ENV === "production" || hasDist) {
    app.use(import_express.default.static(distPath, {
      setHeaders: (res, pathStr) => {
        if (pathStr.endsWith("index.html")) {
          res.setHeader("Cache-Control", "public, max-age=0, must-revalidate");
        } else if (pathStr.includes("/assets/")) {
          res.setHeader("Cache-Control", "public, max-age=31536000, immutable");
        }
      }
    }));
    app.get("*", (req, res) => {
      res.setHeader("Cache-Control", "public, max-age=0, must-revalidate");
      res.sendFile(import_path.default.join(distPath, "index.html"));
    });
  } else {
    const vite = await (0, import_vite.createServer)({
      server: { middlewareMode: true },
      appType: "spa"
    });
    app.use(vite.middlewares);
  }
  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT}`);
  });
}
startServer();
//# sourceMappingURL=server.cjs.map
