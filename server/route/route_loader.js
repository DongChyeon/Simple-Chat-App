import { imageUpload } from "../middleware/multer_middleware.js"

const route_loader = {}

route_loader.init = (app, router) => {
    return initRoutes(app, router)
};

function initRoutes(app, router) {
    router.post('/upload', imageUpload.single('image'), (req, res) => {
        console.log(req.file)

        const imageData = {
            imageUri: req.file.filename
        }
        res.send(JSON.stringify(imageData))
    })

    app.use('/', router)
}

export default route_loader