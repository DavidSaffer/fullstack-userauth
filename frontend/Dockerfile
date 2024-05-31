# Step 1: Use the official Node.js 18 Alpine base image for the builder stage
FROM node:18-alpine as builder

# Step 2: Set the working directory
WORKDIR /app

# Step 3: Copy package.json and package-lock.json
COPY package*.json ./

# Step 4: Install dependencies
RUN npm install

# Step 5: Copy the rest of your Next.js app
COPY . .

# Step 6: Build the Next.js application
RUN npm run build

# Step 7: Use the official Node.js 18 Alpine base image for the runner stage
FROM node:18-alpine

# Step 8: Set the working directory
WORKDIR /app

# Step 9: Copy the build artifacts from the builder stage
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json

# Step 10: Expose the port Next.js runs on
EXPOSE 3000

# Step 11: Define the command to run your app
CMD ["npm", "start"]
