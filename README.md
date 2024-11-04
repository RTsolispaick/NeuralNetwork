# Neural Network Figure Classifier

Это приложение на основе нейронной сети предназначено для классификации изображений фигур. Программа предоставляет веб-интерфейс для загрузки изображений и получения предсказаний о типах фигур.

## Установка

1. Убедитесь, что у вас установлена последняя версия Java (рекомендуется Java 17 или выше).
2. Скачайте файл JAR: [Скачать JAR файл](https://github.com/RTsolispaick/NeuralNetwork/raw/refs/heads/main/neuralNetwork.jar)

## Запуск приложения

Для запуска приложения выполните следующую команду в терминале:

```bash
java -jar neuralNetwork.jar
```

## Использование 

### Загружайте изображения фигур

После запуска приложения откройте веб-браузер и перейдите по адресу:

```url
http://localhost:8080/
```

1. На главной странице вы увидите форму для загрузки изображений фигур. 
2. Выберите изображение и нажмите кнопку `Predict` для отправки. 
3. Приложение обработает изображение и вернет предсказание типа фигуры.

### Просмотр отчета о корректности предсказаний

Перейдите по следующему адресу для просмотра отчета о предсказаниях:

```url
http://localhost:8080/valid
```
На этой странице вы увидите отчет, включающий:

1. Общее количество обработанных изображений 
2. Количество правильных и неправильных предсказаний 
3. Процент правильных предсказаний
