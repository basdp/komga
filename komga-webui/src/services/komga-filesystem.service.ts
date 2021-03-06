import { AxiosInstance } from 'axios'

const API_FILESYSTEM = '/api/v1/filesystem'

export default class KomgaFilesystemService {
  private http: AxiosInstance;

  constructor (http: AxiosInstance) {
    this.http = http
  }

  async getDirectoryListing (path: String = ''): Promise<DirectoryListingDto> {
    try {
      return (await this.http.get(API_FILESYSTEM,
        {
          params: {
            path: path
          }
        }
      )).data
    } catch (e) {
      let msg = 'An error occurred while trying to retrieve directory listing'
      if (e.response.data.message) {
        msg += `: ${e.response.data.message}`
      }
      throw new Error(msg)
    }
  }
}
