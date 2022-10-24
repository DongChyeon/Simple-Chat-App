import multer from 'multer'
import path from 'path'

const __dirname = path.resolve()

export const imageUpload = multer({
    storage: multer.diskStorage({
        destination : (req, file, cb) => {
            cb(null, `${__dirname}/images`);
        },
        filename : (req, file, cb) => {
            var filename = file.fieldname + '-' + Date.now()
            var mimetype;
			switch (file.mimetype) {
				case 'image/jpeg':
					mimetype = 'jpg';
					break;
				case 'image/png':
					mimetype = 'png';
					break;
				case 'image/gif':
					mimetype = 'gif';
					break;
				case 'image/bmp':
					mimetype = 'bmp';
					break;
				default:
					mimetype = 'jpg';
					break;
			}

            cb(null, filename + '.' + mimetype)
        },
    }),
    // 파일 최대 크기를 10MB로 제한
    limits : { fileSize : 10 * 1024 * 1024 }
})