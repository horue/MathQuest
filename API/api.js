// api/placar.js
const fs = require('fs');
const path = require('path');

const filePath = path.join(process.cwd(), 'placar_g.txt');

export default function handler(req, res) {
    // Lê o arquivo placar_g.txt
    if (req.method === 'GET') {
        fs.readFile(filePath, 'utf8', (err, data) => {
            if (err) {
                return res.status(500).json({ error: 'Erro ao ler o arquivo' });
            }
            res.setHeader('Content-Type', 'text/plain');
            res.status(200).send(data); // Retorna o conteúdo do arquivo
        });
    } 
    // Adiciona nova pontuação ao arquivo placar_g.txt
    else if (req.method === 'POST') {
        const { nome, pontuacao } = req.body;

        if (!nome || !pontuacao) {
            return res.status(400).json({ error: 'Nome e pontuação são obrigatórios' });
        }

        const entry = `${nome} - ${pontuacao}\n`;

        fs.appendFile(filePath, entry, (err) => {
            if (err) {
                return res.status(500).json({ error: 'Erro ao escrever no arquivo' });
            }
            res.status(200).json({ message: 'Pontuação adicionada com sucesso' });
        });
    } else {
        res.setHeader('Allow', ['GET', 'POST']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}
