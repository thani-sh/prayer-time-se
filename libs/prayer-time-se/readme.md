# prayer-time-se

Islamic prayer times for Sweden for year 2025 as a JavaScript Module.

## Getting Started

Install the module:

```sh
npm install @thani-sh/prayer-time-se
```

Use exported functions to get prayer times.

```js
import { forDate, forToday } from "@thani-sh/prayer-time-se";

const times = forToday();
// {
//   fajr: { hour: 4, minute: 46 },
//   sunrise: { hour: 6, minute: 2 },
//   dhuhr: { hour: 24, minute: 8 },
//   asr: { hour: 15, minute: 14 },
//   maghrib: { hour: 18, minute: 13 },
//   isha: { hour: 19, minute: 23}]
// }
```

## Contributing

Pull requests are welcome. Please open a Github issue if you have any questions.

## License

[MIT](https://choosealicense.com/licenses/mit/)
