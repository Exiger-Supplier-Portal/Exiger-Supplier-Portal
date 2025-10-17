# Stage 1: Build Next.js app
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .

RUN npm run build

# Stage 2: Run Next.js in production mode
FROM node:20-alpine AS runner
WORKDIR /app

# Copy built files and dependencies
COPY --from=build /app/package*.json ./
COPY --from=build /app/.next ./.next
COPY --from=build /app/public ./public
COPY --from=build /app/node_modules ./node_modules

# Set environment and expose port
ENV NODE_ENV=production
EXPOSE 3000

CMD ["npm", "start"]