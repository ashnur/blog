module.exports = {
  apps: [
    {
      name: 'server:dev',
      script: 'node build/server.js',
      watch: ['build/server.js'],
      env: {
        NODE_ENV: 'development'
      }
    },
    {
      name: 'server:prod',
      script: 'node build/server.js',
      watch: ['build/server.js'],
      env: {
        NODE_ENV: 'production'
      }
    }
  ]
}
