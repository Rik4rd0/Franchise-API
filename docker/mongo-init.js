// Script de inicialización de MongoDB para Franchise API
// Este script se ejecuta automáticamente cuando se inicia el contenedor de MongoDB

// Crear la base de datos y usuario específico para la aplicación
db = db.getSiblingDB('franchise-api-db');

// Crear usuario para la aplicación
db.createUser({
  user: 'franchise-user',
  pwd: 'franchise-pass',
  roles: [
    {
      role: 'readWrite',
      db: 'franchise-api-db'
    }
  ]
});

// Crear colecciones con validación básica
db.createCollection('franchises', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['name', 'branches'],
      properties: {
        name: {
          bsonType: 'string',
          description: 'Nombre de la franquicia - requerido'
        },
        branches: {
          bsonType: 'array',
          description: 'Lista de sucursales - requerida'
        }
      }
    }
  }
});

// Crear índices para optimizar consultas
db.franchises.createIndex({ 'name': 1 }, { unique: true });
db.franchises.createIndex({ 'branches.name': 1 });
db.franchises.createIndex({ 'branches.products.name': 1 });

// Datos de ejemplo (opcional)
db.franchises.insertMany([
  {
    name: 'McDonald\'s',
    branches: [
      {
        id: '1',
        name: 'Sucursal Centro',
        franchiseId: 'franchise-1',
        products: [
          {
            id: 'product-1',
            name: 'Big Mac',
            stock: 50,
            branchId: '1'
          },
          {
            id: 'product-2',
            name: 'Papas Fritas',
            stock: 100,
            branchId: '1'
          }
        ]
      }
    ]
  },
  {
    name: 'KFC',
    branches: [
      {
        id: '2',
        name: 'Sucursal Norte',
        franchiseId: 'franchise-2',
        products: [
          {
            id: 'product-3',
            name: 'Pollo Original',
            stock: 30,
            branchId: '2'
          }
        ]
      }
    ]
  }
]);

print('Base de datos franchise-api-db inicializada correctamente');
