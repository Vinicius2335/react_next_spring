<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Código de Segurança - Loja Sakai</title>
    <style>
        /* Adicione estilos personalizados aqui, como cores e fontes */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
        }

        p {
            font-size: 16px;
            line-height: 1.6;
            color: #666;
        }

        .code-box {
            border: 2px solid #007BFF;
            background-color: #fff;
            padding: 10px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            border-radius: 5px;
            margin-top: 20px;
        }

        .footer {
            margin-top: 20px;
            text-align: center;
            color: #777;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Código de Segurança - Loja Sakai</h1>
        <p>Olá, ${nome}</p>

        <p>Recebemos uma solicitação para gerar um código de segurança para a sua conta na nossa loja virtual.</p>
        <p>Seu código de segurança é:</p>
        <div class="code-box">${codigo}</div>
        <p>Este código expirará em 15 minutos.</p>

        <p>Este código é importante para garantir a segurança da sua conta. Por favor, não compartilhe com ninguém.</p>
        <p>Se você não solicitou este código, por favor, entre em contato conosco imediatamente.</p>

        <div class="footer">
            <p>Atenciosamente,</p>
            <p>Equipe da Loja Sakai</p>
        </div>
    </div>
</body>
</html>
