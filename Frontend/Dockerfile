FROM node:20

WORKDIR /app

COPY . .

RUN npm install -g @angular/cli

RUN npm install

ENV HOST=0.0.0.0

EXPOSE 4200

CMD ["npm", "run", "start"]
